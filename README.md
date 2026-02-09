# PluginHide

Lightweight Minecraft plugin for **hiding plugin commands**  
and preventing information leaks through tab-completion.

Designed as a **clean, fast alternative to PLHide**, without unnecessary overhead.

---

## Overview

PluginHide completely removes plugin-related commands from player visibility.

For regular players, hidden commands:
- do not appear in TAB-completion
- cannot be executed
- behave as if they **do not exist on the server**

Instead of Bukkit’s default error, the plugin sends a **custom configurable message**
(e.g. `Command not found`).

---

## Core Goal

> Prevent players from discovering installed plugins.

This includes:
- `/plugins`, `/pl`, `/version`
- `plugin:command` namespace usage
- tab-complete plugin enumeration

Performance and simplicity are the main priorities.

---

## Features

- Removes hidden commands from **TAB-completion**
- Blocks execution of hidden commands
- Fully masks plugin namespaces (`plugin:command`)
- Custom replacement message instead of Bukkit errors
- Two operating modes: **BLACKLIST / WHITELIST**
- Bypass permission for admins
- No dependencies
- Minimal performance impact

---

## How It Works

PluginHide operates on two levels:

### 1. Tab-Complete Filtering (Primary)

Hidden commands are removed **before** they are sent to the player,
preventing plugin discovery via TAB.

### 2. Command Execution Masking

If a player manually types a hidden command:
- execution is cancelled
- a custom message is sent
- Bukkit’s `Unknown command` message is suppressed

To the player, the command simply does not exist.

---

## Modes

### BLACKLIST

Only commands listed in `commands` are hidden.

```yml
mode: BLACKLIST
commands:
  - pl
  - plugins
  - version
  - ver

Recommended for most servers.
WHITELIST

Only commands listed in commands are allowed.

All other commands are hidden and masked.

mode: WHITELIST
commands:
  - help
  - spawn
  - msg

Useful for hubs, lobbies, or restricted environments.
Configuration
config.yml

mode: BLACKLIST

commands:
  - pl
  - plugins
  - version
  - ver
  - luckperms
  - lp

bypass-permission: pluginhide.bypass

case-sensitive: false

messages.yml

command-not-found:
  enabled: true
  message: "&cCommand not found."

Supports:

    & color codes

    RGB (#RRGGBB)

Commands

/pluginhide reload

Reloads configuration without restarting the server.
Permissions

    pluginhide.bypass
    Full access. Commands are visible in TAB and not masked.

    pluginhide.reload
    Allows /pluginhide reload.

Compatibility

    Minecraft 1.16.5

    Tested on:

        Paper

        Purpur

        Spigot

Java 8+

License MIT
