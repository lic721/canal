package com.test.mysql;

import java.util.Arrays;

public class HandshakePayload {

    private byte   protocolVersion = MySqlConnectorSample.DEFAULT_PROTOCOL_VERSION;
    private String serverVersion;
    private int   threadId;
    private byte[] seed;
    private int    serverCapabilities;
    private byte   serverCharsetNumber;
    private int    serverStatus;
    private byte[] restOfScrambleBuff;
    private byte[] authPluginName;

    public byte getProtocolVersion() {
        return protocolVersion;
    }

    public void setProtocolVersion(byte protocolVersion) {
        this.protocolVersion = protocolVersion;
    }

    public String getServerVersion() {
        return serverVersion;
    }

    public void setServerVersion(String serverVersion) {
        this.serverVersion = serverVersion;
    }

    public int getThreadId() {
        return threadId;
    }

    public void setThreadId(int threadId) {
        this.threadId = threadId;
    }

    public byte[] getSeed() {
        return seed;
    }

    public void setSeed(byte[] seed) {
        this.seed = seed;
    }

    public int getServerCapabilities() {
        return serverCapabilities;
    }

    public void setServerCapabilities(int serverCapabilities) {
        this.serverCapabilities = serverCapabilities;
    }

    public byte getServerCharsetNumber() {
        return serverCharsetNumber;
    }

    public void setServerCharsetNumber(byte serverCharsetNumber) {
        this.serverCharsetNumber = serverCharsetNumber;
    }

    public int getServerStatus() {
        return serverStatus;
    }

    public void setServerStatus(int serverStatus) {
        this.serverStatus = serverStatus;
    }

    public byte[] getRestOfScrambleBuff() {
        return restOfScrambleBuff;
    }

    public void setRestOfScrambleBuff(byte[] restOfScrambleBuff) {
        this.restOfScrambleBuff = restOfScrambleBuff;
    }

    public byte[] getAuthPluginName() {
        return authPluginName;
    }

    public void setAuthPluginName(byte[] authPluginName) {
        this.authPluginName = authPluginName;
    }

    @Override
    public String toString() {
        return "HandshakePayload{" + "protocolVersion=" + protocolVersion + ", serverVersion='" + serverVersion + '\''
               + ", threadId=" + threadId + ", seed=" + Arrays.toString(seed) + ", serverCapabilities="
               + serverCapabilities + ", serverCharsetNumber=" + serverCharsetNumber + ", serverStatus=" + serverStatus
               + ", restOfScrambleBuff=" + Arrays.toString(restOfScrambleBuff) + ", authPluginName="
               + Arrays.toString(authPluginName) + '}';
    }
}
