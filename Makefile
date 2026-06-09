OS := $(shell uname -s 2>/dev/null || echo Windows)

ifeq ($(OS), Windows)
	COMPOSE = docker compose --env-file ./backend/.env -f compose.yml -f compose.windows.yml
else
	COMPOSE = docker compose --env-file ./backend/.env -f compose.yml -f compose.linux.yml
endif

up:
	$(COMPOSE) up

down:
	$(COMPOSE) down

down-clean:
	$(COMPOSE) down -v
