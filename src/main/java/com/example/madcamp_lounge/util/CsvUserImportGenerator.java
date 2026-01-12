package com.example.madcamp_lounge.util;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public final class CsvUserImportGenerator {
    private CsvUserImportGenerator() {
    }

    public static void main(String[] args) throws IOException {
        if (args.length != 1 || args[0].isBlank()) {
            System.out.println("Usage: ./gradlew generateUserSql -Pcsv=/path/to/users.csv");
            return;
        }

        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        try (BufferedReader reader = new BufferedReader(new FileReader(args[0]))) {
            String headerLine = readRecord(reader);
            if (headerLine == null) {
                return;
            }

            char delimiter = detectDelimiter(headerLine);
            List<String> headers = parseCsvLine(headerLine, delimiter);
            Map<String, Integer> index = new HashMap<>();
            for (int i = 0; i < headers.size(); i++) {
                index.put(normalizeHeader(headers.get(i)), i);
            }

            String record;
            while ((record = readRecord(reader)) != null) {
                if (record.isBlank()) {
                    continue;
                }
                List<String> fields = parseCsvLine(record, delimiter);
                String name = value(fields, index, "이름");
                String mbti = value(fields, index, "MBTI");
                String introduction = value(fields, index, "introduction");
                String classSection = value(fields, index, "분반");
                String university = value(fields, index, "출신대학");
                String hobby = value(fields, index, "취미");
                String loginId = value(fields, index, "ID");
                String password = value(fields, index, "PW");

                if (loginId == null || password == null) {
                    continue;
                }

                String hashed = encoder.encode(password);
                System.out.println(
                    "INSERT INTO users (login_id, password, name, nickname, mbti, class_section, hobby, introduction, university, is_first_login, created_at) "
                        + "VALUES ("
                        + sqlString(loginId) + ", "
                        + sqlString(hashed) + ", "
                        + sqlString(name) + ", "
                        + sqlString(name) + ", "
                        + sqlString(mbti) + ", "
                        + sqlString(classSection) + ", "
                        + sqlString(hobby) + ", "
                        + sqlString(introduction) + ", "
                        + sqlString(university) + ", "
                        + "TRUE, NOW());"
                );
            }
        }
    }

    private static String value(List<String> fields, Map<String, Integer> index, String key) {
        Integer idx = index.get(normalizeHeader(key));
        if (idx == null || idx >= fields.size()) {
            return null;
        }
        String value = fields.get(idx);
        if (value == null || value.isBlank()) {
            return null;
        }
        return value.trim();
    }

    private static String sqlString(String value) {
        if (value == null) {
            return "NULL";
        }
        return "'" + value.replace("'", "''") + "'";
    }

    private static List<String> parseCsvLine(String line, char delimiter) {
        List<String> result = new ArrayList<>();
        StringBuilder current = new StringBuilder();
        boolean inQuotes = false;
        for (int i = 0; i < line.length(); i++) {
            char ch = line.charAt(i);
            if (ch == '"') {
                if (inQuotes && i + 1 < line.length() && line.charAt(i + 1) == '"') {
                    current.append('"');
                    i++;
                } else {
                    inQuotes = !inQuotes;
                }
            } else if (ch == delimiter && !inQuotes) {
                result.add(current.toString());
                current.setLength(0);
            } else {
                current.append(ch);
            }
        }
        result.add(current.toString());
        return result;
    }

    private static String readRecord(BufferedReader reader) throws IOException {
        String line = reader.readLine();
        if (line == null) {
            return null;
        }
        StringBuilder record = new StringBuilder(line);
        while (!isQuotesBalanced(record.toString())) {
            String next = reader.readLine();
            if (next == null) {
                break;
            }
            record.append("\n").append(next);
        }
        return record.toString();
    }

    private static boolean isQuotesBalanced(String text) {
        int quoteCount = 0;
        for (int i = 0; i < text.length(); i++) {
            if (text.charAt(i) == '"') {
                if (i + 1 < text.length() && text.charAt(i + 1) == '"') {
                    i++;
                } else {
                    quoteCount++;
                }
            }
        }
        return quoteCount % 2 == 0;
    }

    private static char detectDelimiter(String headerLine) {
        if (headerLine.indexOf('\t') >= 0 && headerLine.indexOf(',') < 0) {
            return '\t';
        }
        return ',';
    }

    private static String normalizeHeader(String header) {
        if (header == null) {
            return "";
        }
        String trimmed = header.trim();
        if (!trimmed.isEmpty() && trimmed.charAt(0) == '\uFEFF') {
            trimmed = trimmed.substring(1);
        }
        return trimmed;
    }
}
