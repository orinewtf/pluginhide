# PluginHide

Lightweight plugin for Spigot/Paper 1.16.5 that hides commands from regular players to reduce plugin information leaks.

## Features

- Filters hidden commands from TAB-completion
- Blocks execution of hidden commands
- Masks namespace commands (`plugin:command`)
- Sends custom replacement message instead of default unknown-command output
- Supports `BLACKLIST` and `WHITELIST` modes
- Supports bypass permission for admins
- No external dependencies

## Compatibility

- Spigot / Paper 1.16.5
- Java 8+

## Command

- `/cmdmask reload` - reload plugin configuration

## Permissions

- `pluginhide.bypass` - bypass TAB filtering and command masking
- `pluginhide.reload` - allows using `/cmdmask reload`

## Configuration (`config.yml`)

```yml
mode: BLACKLIST
bypass-permission: pluginhide.bypass
commands:
  - pl
  - plugins
  - version
  - ver
  - luckperms
  - lp

command-not-found:
  enabled: true
  message: "{prefix}&cКоманда не найдена."

messages:
  prefix: "&b&lPluginHide &8>> &7"
  no-permission: "{prefix}&cНедостаточно прав."
  reload-success: "{prefix}&aКонфигурация перезагружена."
  usage: "{prefix}&7Использование: &f/{label} reload"
```

## Modes

### BLACKLIST

Commands listed in `commands` are hidden and blocked.

### WHITELIST

Only commands listed in `commands` are visible and executable; all others are hidden and blocked.

## Author

- `orine`

## License MIT
