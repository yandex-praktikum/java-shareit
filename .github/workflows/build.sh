cp -rf ./tests/checkstyle.xml ./checkstyle.xml
mvn verify -P check,coverage --no-transfer-progress