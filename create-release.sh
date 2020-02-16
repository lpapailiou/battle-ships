#!/bin/sh

curl \
    --header 'Content-Type: application/json' \
    --header 'JOB-TOKEN: ${CI_JOB_TOKEN}' \
    --data "{
        "name": "BattleShips Release ${CI_COMMIT_REF_NAME}",
        "tag_name": "${CI_COMMIT_REF_NAME}",
        "description": "${CI_COMMIT_MESSAGE}",
        "assets": {
            "links":
            [{
                "name": "battleships.apk",
                "url": "https://gitlab.com/api/v4/projects/${CI_PROJECT_ID/jobs/${CI_JOB_ID}/artifacts"
            }]
        }
    }" \
    --request POST https://gitlab.com/api/v4/projects/${CI_PROJECT_ID}/releases"
