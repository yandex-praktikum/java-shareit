nohup mvn spring-boot:run >> console.log 2>&1 &
chmod a+x ./tests/.github/workflows/wait-for-it.sh
./tests/.github/workflows/wait-for-it.sh -t 60 localhost:8080