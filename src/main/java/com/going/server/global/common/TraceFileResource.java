package com.going.server.global.common;

import org.springframework.core.io.ByteArrayResource;

public class TraceFileResource extends ByteArrayResource {
    private final String fileName;

    public TraceFileResource(byte[] byteArray, String fileName) {
        super(byteArray);
        this.fileName = fileName;
    }

    @Override
    public String getFilename() {
        return fileName;
    }
}
