cp -rf ./tests/checkstyle.xml ./checkstyle.xml
cp -rf ./tests/suppressions.xml ./suppressions.xml
mvn enforcer:enforce -Drules=requireProfileIdsExist -P check --no-transfer-progress
mvn verify -P check,coverage --no-transfer-progress
docker-compose build