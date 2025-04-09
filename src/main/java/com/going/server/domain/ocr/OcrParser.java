package com.going.server.domain.ocr;

import java.util.Map;

public interface OcrParser {
    Map<String, String> parse(String response);
}
