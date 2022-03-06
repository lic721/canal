package com.test.mysql;

import java.io.*;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.apache.commons.lang.StringUtils;

public class MySqlConnectorSample {

    public static final byte NULL_TERMINATED_STRING_DELIMITER = 0x00;
    public static final byte DEFAULT_PROTOCOL_VERSION         = 0x0a;
    public static final int  MAX_PACKET_LENGTH                = (1 << 24);     // 即2的24次方

    static String            dbIp                             = "10.181.24.56";
    static int               dbPort                           = 3306;
    static String            dbName                           = null;          // "saasdemo";
    static String            user                             = "canal";
    static String            password                         = "123456";

    public static void main(String[] args) throws IOException {

        byte a = (byte) 0xFF;
        System.out.println(a);// -1,表面上是byte,实际上是int,32位bit全是1
        System.out.println((byte) (a >>> 8));// -1,证明了表面上是byte,实际上是int
        System.out.println(a & 0xFF);// 255,保证了最后8位bit是1,其余是0,表面上是byte,实际上是int

        SocketAddress address = new InetSocketAddress(dbIp, dbPort);
        Socket socket = new Socket();
        socket.setTcpNoDelay(true);
        socket.setKeepAlive(true);
        socket.setReuseAddress(true);
        socket.connect(address, 5000);
        InputStream input = new BufferedInputStream(socket.getInputStream(), 16384);
        OutputStream output = socket.getOutputStream();

        System.out.println("======第一步. 与master建立连接");
        connectMaster(input, output);

        System.out.println("======第二步. 发送register_slave命令");
        int serverId = sendRegisterSlave(socket, input, output);

        System.out.println("======第三步. 发送dump命令");
        // 1 [12] COM_BINLOG_DUMP
        // 4 binlog-pos
        // 2 flags
        // 4 server-id
        // string[EOF] binlog-filename
        ByteArrayOutputStream body = new ByteArrayOutputStream();
        body.write((byte) 0x12);
        int pos = 4;
        body.write(pos & 0xFF);
        body.write(pos >>> 8 & 0xFF);
        body.write(pos >>> 16 & 0xFF);
        body.write(pos >>> 24 & 0xFF);
        body.write(0x01);
        body.write(0x00);
        body.write(serverId & 0xFF);
        body.write(serverId >>> 8 & 0xFF);
        body.write(serverId >>> 16 & 0xFF);
        body.write(serverId >>> 24 & 0xFF);
        body.write("mysql-bin.000001".getBytes(StandardCharsets.UTF_8));

        byte[] bodyBytes = body.toByteArray();
        output.write(bodyBytes.length & 0xFF);
        output.write(bodyBytes.length >>> 8 & 0xFF);
        output.write(bodyBytes.length >>> 16 & 0xFF);
        output.write((byte) 0x00);// sequence 0
        output.write(bodyBytes);
        byte[] payloadArr = readAndGetPayloadArr(input);
        if (payloadArr[0] == 0) {
            System.out.println("payload首位为0,成功!");
            for (int i = 1; i < payloadArr.length; i++) {
                System.out.print((char) payloadArr[i]);
            }
        } else {
            System.out.println("失败!");
            for (int i = 1; i < payloadArr.length; i++) {
                System.out.print((char) payloadArr[i]);
            }
        }

        socket.shutdownInput();
        socket.shutdownOutput();
        socket.close();
    }

    private static int sendRegisterSlave(Socket socket, InputStream input, OutputStream output) throws IOException {
        ByteArrayOutputStream body = new ByteArrayOutputStream();
        // 1 [15] COM_REGISTER_SLAVE
        // 4 server-id
        // 1 slaves hostname length
        // string[$len] slaves hostname
        // 1 slaves user len
        // string[$len] slaves user
        // 1 slaves password len
        // string[$len] slaves password
        // 2 slaves mysql-port
        // 4 replication rank
        // 4 master-id
        body.write((byte) 0x15);
        // server-id
        int serverId = Math.abs(socket.getLocalAddress().getHostName().hashCode());
        body.write((byte) (serverId & 0xFF));
        body.write((byte) (serverId >>> 8 & 0xFF));
        body.write((byte) (serverId >>> 16 & 0xFF));
        body.write((byte) (serverId >>> 24 & 0xFF));
        // slaves hostname length
        InetSocketAddress localSocketAddress = (InetSocketAddress) socket.getLocalSocketAddress();
        byte[] host = localSocketAddress.getHostString().getBytes();
        body.write((byte) host.length);
        // slaves hostname
        body.write(host);
        // slaves user len
        body.write((byte) user.getBytes().length);
        // slaves user
        body.write(user.getBytes());
        // slaves password len
        body.write((byte) password.getBytes().length);
        // slaves password
        body.write(password.getBytes());
        // slaves mysql-port
        body.write((byte) (localSocketAddress.getPort() & 0xFF));
        body.write((byte) (localSocketAddress.getPort() >>> 8 & 0xFF));
        // replication rank
        body.write((byte) 0x00);
        body.write((byte) 0x00);
        body.write((byte) 0x00);
        body.write((byte) 0x00);
        // master-id
        body.write((byte) 0x00);
        body.write((byte) 0x00);
        body.write((byte) 0x00);
        body.write((byte) 0x00);

        byte[] bodyBytes = body.toByteArray();
        byte[] header = new byte[4];
        header[0] = ((byte) (bodyBytes.length & 0xFF));
        header[1] = ((byte) (bodyBytes.length >>> 8 & 0xFF));
        header[2] = ((byte) (bodyBytes.length >>> 16 & 0xFF));
        header[3] = ((byte) 0x00);

        output.write(header);
        output.write(bodyBytes);
        System.out.println("发送COM_REGISTER_SLAVE完毕");

        byte[] payloadArr = readAndGetPayloadArr(input);
        if (payloadArr[0] == 0) {
            System.out.println("payload首位为0,register slave成功!");
        } else {
            System.out.println("register slave失败!");
            for (int i = 1; i < payloadArr.length; i++) {
                System.out.print((char) payloadArr[i]);
            }
        }

        return serverId;
    }

    private static void connectMaster(InputStream input, OutputStream output) throws IOException {
        System.out.println("服务器socket连接成功");
        // 接收handshake信息
        HandshakePayload handshakePayload = receiveHandshake(input);
        System.out.println("接收Handshake信息完毕");

        // 发送HandshakeResponse
        byte[] handshakeResponse = generateHandshakeResponse(handshakePayload);
        byte[] header = new byte[4];
        header[0] = (byte) (handshakeResponse.length & 0xFF);
        header[1] = (byte) (handshakeResponse.length >>> 8);
        header[2] = (byte) (handshakeResponse.length >>> 16);
        header[3] = 0x01;

        output.write(header);
        output.write(handshakeResponse);
        System.out.println("发送HandshakeResponse完毕");

        byte[] payloadArr = readAndGetPayloadArr(input);
        if (payloadArr[0] == 0) {
            System.out.println("payload首位为0,数据库连接成功!");
        } else {
            System.out.println("数据库连接失败!");
            for (int i = 1; i < payloadArr.length; i++) {
                System.out.print((char) payloadArr[i]);
            }
        }
    }

    private static byte[] readAndGetPayloadArr(InputStream input) throws IOException {
        byte[] payloadLengthArr = readBytes(input, 3);
        int payloadLength = (payloadLengthArr[0] & 0xFF) | ((payloadLengthArr[1] & 0xFF) << 8)
                            | ((payloadLengthArr[2] & 0xFF) << 16);
        System.out.println("    payloadLength:" + payloadLength);

        byte[] sequenceIdArr = readBytes(input, 1);
        int sequenceId = (sequenceIdArr[0]);
        System.out.println("    sequenceId:" + sequenceId);

        byte[] payloadArr = readBytes(input, payloadLength);
        return payloadArr;
    }

    private static HandshakePayload receiveHandshake(InputStream input) throws IOException {
        byte[] payloadLengthArr = readBytes(input, 3);
        int payloadLength = (payloadLengthArr[0] & 0xFF) | (payloadLengthArr[1] << 8) | (payloadLengthArr[2] << 16);
        System.out.println("    payloadLength:" + payloadLength);

        byte[] sequenceIdArr = readBytes(input, 1);
        int sequenceId = (sequenceIdArr[0]);
        System.out.println("    sequenceId:" + sequenceId);

        byte[] payloadArr = readBytes(input, payloadLength);
        HandshakePayload handshakePayload = getHandshakePayload(payloadArr);
        System.out.println("    payload:" + handshakePayload);
        return handshakePayload;
    }

    public static byte[] readNullTerminatedBytes(byte[] data, int index) {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        for (int i = index; i < data.length; i++) {
            byte item = data[i];
            if (item == NULL_TERMINATED_STRING_DELIMITER) {
                break;
            }
            out.write(item);
        }
        return out.toByteArray();
    }

    /**
     * <pre>
     * VERSION 4.1
     *  Bytes                        Name
     *  -----                        ----
     *  4                            client_flags
     *  4                            max_packet_size
     *  1                            charset_number
     *  23                           (filler) always 0x00...
     *  n (Null-Terminated String)   user
     *  n (Length Coded Binary)      scramble_buff (1 + x bytes)
     *  n (Null-Terminated String)   databasename (optional)
     *  n (Null-Terminated String)   auth plugin name (optional)
     * </pre>
     */
    public static byte[] generateHandshakeResponse(HandshakePayload handshakePayload) throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        // 1. write client_flags
        // 参见ClientAuthenticationPacket中的clientCapability
        handshakePayload.setServerCapabilities(632325);
        out.write((byte) (handshakePayload.getServerCapabilities() & 0xFF));
        out.write((byte) (handshakePayload.getServerCapabilities() >>> 8) & 0xFF);
        out.write((byte) (handshakePayload.getServerCapabilities() >>> 16) & 0xFF);
        out.write((byte) (handshakePayload.getServerCapabilities() >>> 24) & 0xFF);
        // client_interactive
        // feature

        // 2. write max_packet_size
        out.write((byte) (MAX_PACKET_LENGTH & 0xFF));
        out.write((byte) (MAX_PACKET_LENGTH >>> 8) & 0xFF);
        out.write((byte) (MAX_PACKET_LENGTH >>> 16) & 0xFF);
        out.write((byte) (MAX_PACKET_LENGTH >>> 24) & 0xFF);
        // 3. write charset_number
        // utf8_general_ci
        out.write(0x21);
        // 4. write (filler) always 0x00...
        out.write(new byte[23]);
        // 5. write (Null-Terminated String) user
        out.write(user.getBytes(StandardCharsets.UTF_8));
        out.write(NULL_TERMINATED_STRING_DELIMITER);
        // 6. write (Length Coded Binary) scramble_buff (1 + x bytes)
        if (StringUtils.isEmpty(password)) {
            out.write(0x00);
        } else {
            try {
                byte[] encryptedPassword = scramble411(password.getBytes(StandardCharsets.UTF_8),
                    joinAndCreateScrumbleBuff(handshakePayload));
                writeBinaryCodedLengthBytes(encryptedPassword, out);
            } catch (NoSuchAlgorithmException e) {
                throw new RuntimeException("can't encrypt password that will be sent to MySQL server.", e);
            }
        }
        // 7 . (Null-Terminated String) databasename (optional)
        // if (dbName != null) {
        // out.write(dbName.getBytes());
        // out.write(NULL_TERMINATED_STRING_DELIMITER);
        // }

        // 8 . (Null-Terminated String) auth plugin name (optional)
        out.write("mysql_native_password".getBytes());
        out.write(NULL_TERMINATED_STRING_DELIMITER);
        // end write
        return out.toByteArray();
    }

    public static void writeBinaryCodedLengthBytes(byte[] data, ByteArrayOutputStream out) throws IOException {
        // 1. write length byte/bytes
        if (data.length < 252) {
            out.write((byte) data.length);
        } else if (data.length < (1 << 16L)) {
            out.write((byte) 252);
            out.write((byte) (data.length & 0xFF));
            out.write((byte) ((data.length >>> 8) & 0xFF));
        } else if (data.length < (1 << 24L)) {
            out.write((byte) 253);
            out.write((byte) (data.length & 0xFF));
            out.write((byte) ((data.length >>> 8) & 0xFF));
            out.write((byte) ((data.length >>> 16) & 0xFF));
        } else {
            out.write((byte) 254);
            out.write((byte) (data.length & 0xFF));
            out.write((byte) (data.length >>> 8));
            out.write((byte) (data.length >>> 16));
            out.write((byte) (data.length >>> 24));
        }
        // 2. write real data followed length byte/bytes
        out.write(data);
    }

    private static byte[] joinAndCreateScrumbleBuff(HandshakePayload handshakePayload) throws IOException {
        byte[] dest = new byte[handshakePayload.getSeed().length + handshakePayload.getRestOfScrambleBuff().length];
        System.arraycopy(handshakePayload.getSeed(), 0, dest, 0, handshakePayload.getSeed().length);
        System.arraycopy(handshakePayload.getRestOfScrambleBuff(),
            0,
            dest,
            handshakePayload.getSeed().length,
            handshakePayload.getRestOfScrambleBuff().length);
        return dest;
    }

    public static final byte[] scramble411(byte[] pass, byte[] seed) throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance("SHA-1");
        byte[] pass1 = md.digest(pass);
        md.reset();
        byte[] pass2 = md.digest(pass1);
        md.reset();
        md.update(seed);
        byte[] pass3 = md.digest(pass2);
        for (int i = 0; i < pass3.length; i++) {
            pass3[i] = (byte) (pass3[i] ^ pass1[i]);
        }
        return pass3;
    }

    /**
     * <pre>
     * Bytes                        Name
     *  -----                        ----
     *  1                            protocol_version
     *  n (Null-Terminated String)   server_version
     *  4                            thread_id
     *  8                            scramble_buff
     *  1                            (filler) always 0x00
     *  2                            server_capabilities
     *  1                            server_language
     *  2                            server_status
     *  13                           (filler) always 0x00 ...
     *  13                           rest of scramble_buff (4.1)
     * </pre>
     */
    public static HandshakePayload getHandshakePayload(byte[] data) {
        HandshakePayload handshakePayload = new HandshakePayload();
        int index = 0;
        // 1. read protocol_version
        handshakePayload.setProtocolVersion(data[index]);
        index++;
        // 2. read server_version
        byte[] serverVersionBytes = readNullTerminatedBytes(data, index);
        handshakePayload.setServerVersion(new String(serverVersionBytes));
        index += (serverVersionBytes.length + 1);
        // 3. read thread_id
        int threadId = (data[index] & 0xFF) | (data[index + 1] & 0xFF << 8) | (data[index + 2] & 0xFF << 16)
                       | (data[index + 3] & 0xFF << 24);
        handshakePayload.setThreadId(threadId);
        index += 4;
        // 4. read scramble_buff
        handshakePayload.setSeed(readFixedLengthBytes(data, index, 8));
        index += 8;
        index += 1; // 1 byte (filler) always 0x00
        // 5. read server_capabilities
        int serverCapabilities = (data[index] & 0xFF) | ((data[index + 1] & 0xFF) << 8);
        handshakePayload.setServerCapabilities(serverCapabilities);
        index += 2;
        if (data.length > index) {
            // 6. read server_language
            handshakePayload.setServerCharsetNumber(data[index]);
            index++;
            // 7. read server_status
            handshakePayload.setServerStatus((data[index] & 0xFF) | (data[index + 1] & 0xFF << 8));
            index += 2;
            // 8. bypass filtered bytes
            int capabilityFlags2 = (data[index] & 0xFF) | (data[index + 1] & 0xFF << 8);
            index += 2;
            int capabilities = (capabilityFlags2 << 16) | serverCapabilities;
            // int authPluginDataLen = -1;
            // if ((capabilities & Capability.CLIENT_PLUGIN_AUTH) != 0) {
            // authPluginDataLen = data[index];
            // }
            index += 1;
            index += 10;
            // 9. read rest of scramble_buff
            if ((capabilities & 0x00008000) != 0) {
                // int len = Math.max(13, authPluginDataLen - 8);
                // this.authPluginDataPart2 =
                // buffer.readFixedLengthString(len);// scramble2

                // Packet规定最后13个byte是剩下的scrumble,
                // 但实际上最后一个字节是0, 不应该包含在scrumble中.
                handshakePayload.setRestOfScrambleBuff(readFixedLengthBytes(data, index, 12));
            }

            index += 12 + 1;
            if ((capabilities & 0x00080000) != 0) {
                handshakePayload.setAuthPluginName(readNullTerminatedBytes(data, index));
            }
            // end read
        }
        return handshakePayload;
    }

    private static byte[] readFixedLengthBytes(byte[] data, int index, int length) {
        byte[] bytes = new byte[length];
        System.arraycopy(data, index, bytes, 0, length);
        return bytes;
    }

    private static byte[] readBytes(InputStream input, int readSize) throws IOException {
        byte[] data = new byte[readSize];
        int remain = readSize;
        while (remain > 0) {
            int read = input.read(data, readSize - remain, remain);
            // -1表示已经读完
            if (read > -1) {
                remain -= read;
            } else {
                // remain>0时, 不可能返回-1
                throw new IOException("EOF encountered.");
            }
        }
        return data;
    }

}
