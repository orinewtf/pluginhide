package me.orine.pluginhide.model;

public final class ParsedCommand {
    public static final ParsedCommand EMPTY = new ParsedCommand("", false);

    private final String normalized;
    private final boolean hadNamespace;

    public ParsedCommand(String normalized, boolean hadNamespace) {
        this.normalized = normalized;
        this.hadNamespace = hadNamespace;
    }

    public String getNormalized() {
        return normalized;
    }

    public boolean hadNamespace() {
        return hadNamespace;
    }

    public boolean isEmpty() {
        return normalized.isEmpty();
    }
}
