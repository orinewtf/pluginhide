package me.orine.pluginhide.service;

import me.orine.pluginhide.model.ParsedCommand;

import java.util.Locale;

public final class CommandNormalizer {

    public ParsedCommand parse(String raw) {
        if (raw == null || raw.isEmpty()) {
            return ParsedCommand.EMPTY;
        }

        int length = raw.length();
        int index = 0;
        while (index < length && raw.charAt(index) == ' ') {
            index++;
        }

        while (index < length && raw.charAt(index) == '/') {
            index++;
        }

        if (index >= length) {
            return ParsedCommand.EMPTY;
        }

        int end = index;
        while (end < length && raw.charAt(end) != ' ') {
            end++;
        }

        if (end <= index) {
            return ParsedCommand.EMPTY;
        }

        String token = raw.substring(index, end);
        int namespacePos = token.lastIndexOf(':');
        boolean hadNamespace = namespacePos >= 0;
        if (hadNamespace) {
            token = token.substring(namespacePos + 1);
        }

        if (token.isEmpty()) {
            return ParsedCommand.EMPTY;
        }

        return new ParsedCommand(token.toLowerCase(Locale.ROOT), hadNamespace);
    }

    public boolean isCommandRootCompletion(String buffer) {
        if (buffer == null || buffer.isEmpty()) {
            return false;
        }

        int length = buffer.length();
        int index = 0;
        while (index < length && buffer.charAt(index) == ' ') {
            index++;
        }
        if (index >= length || buffer.charAt(index) != '/') {
            return false;
        }

        while (index < length && buffer.charAt(index) == '/') {
            index++;
        }

        while (index < length) {
            if (buffer.charAt(index) == ' ') {
                return false;
            }
            index++;
        }

        return true;
    }
}
