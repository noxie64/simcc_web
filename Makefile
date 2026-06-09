OS := $(shell uname -s 2>/dev/null || echo Windows)

ifeq ($(OS), Windows)
	COMPOSE = docker compose --env-file ./backend/.env -f docker-compose.yml -f docker-compose.windows.yml
else
	COMPOSE = docker compose --env-file ./backend/.env
endif

up:
	$(COMPOSE) up

down:
	$(COMPOSE) down

down-clean:
	$(COMPOSE) down -v
