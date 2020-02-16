#!/bin/bash


echo "CI_JOB_ID: '${CI_JOB_ID}'"
echo "CI_PIPELINE_ID: '${CI_PIPELINE_ID}'"
echo "CI_CONCURRENT_ID: '${CI_CONCURRENT_ID}'"
echo "CI_CONCURRENT_PROJECT_ID: '${CI_CONCURRENT_PROJECT_ID}'"
echo "CI_PIPELINE_IID: '${CI_PIPELINE_IID}'"
echo "CI_RUNNER_ID: '${CI_RUNNER_ID}'"

set -x

curl \
    --header 'Content-Type: application/json' \
    --header "JOB-TOKEN: ${CI_JOB_TOKEN}" \
    --data "{ \
        \"name\": \"BattleShips Release ${CI_COMMIT_REF_NAME}\", \
        \"tag_name\": \"${CI_COMMIT_REF_NAME}\", \
        \"description\": \"${CI_COMMIT_MESSAGE/$'\n'/}\", \
        \"assets\": { \
            \"links\": \
            [{ \
                \"name\": \"battleships.apk\", \
                \"url\": \"https://gitlab.com/api/v4/projects/${CI_PROJECT_ID}/jobs/${CI_JOB_ID}/artifacts\" \
            }] \
        } \
    }" \
    --request POST https://gitlab.com/api/v4/projects/${CI_PROJECT_ID}/releases
