# tax-rates

## How to start
Java 21 should be installed and Docker should be up and running on the machine.

After cloning the repository the application can be started by running the `startup.sh` bash script

## Known issues, possible todos
I assumed that the narrowest time period has the highest priority, but there's a known issue: when there are two periods
with the same length always the first will be returned as the valid record.

The problem+json response creation could be a bit more sophisticated and new error types could be introduced.

