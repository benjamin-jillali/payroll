FROM airhacks/payara5
COPY ./target/payroll.war ${DEPLOYMENT_DIR}
