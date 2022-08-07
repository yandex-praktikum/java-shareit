docker-compose up --detach
chmod a+x ./tests/.github/workflows/wait-for-it.sh
./tests/.github/workflows/wait-for-it.sh -t 60 localhost:8080
./tests/.github/workflows/wait-for-it.sh -t 60 localhost:9090
docker-compose logs
