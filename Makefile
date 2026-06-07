up:
	docker compose --env-file ./backend/.env up

up-build:
	docker compose --env-file ./backend/.env up --build

down:
	docker compose --env-file ./backend/.env down

down-clean:
	docker compose --env-file ./backend/.env down -v
