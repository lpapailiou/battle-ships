image: jangrewe/gitlab-ci-android

before_script:
  - export GRADLE_USER_HOME=$(pwd)/.gradle
  - chmod +x ./gradlew

cache:
  key: ${CI_PROJECT_ID}
  paths:
    - .gradle/

stages:
  - sign
  - release


assembleRelease:
  stage: sign
  only:
    - tags

  script:
    - echo $KEYSTORE_BASE64 | base64 -d > keystore.jks
    - ./gradlew assembleRelease
      -Pandroid.injected.signing.store.file=$(pwd)/keystore.jks
      -Pandroid.injected.signing.store.password=$KEYSTORE_PASSWORD
      -Pandroid.injected.signing.key.alias=$GITLAB_KEY_ALIAS
      -Pandroid.injected.signing.key.password=$GITLAB_KEY_PASSWORD
  artifacts:
    paths:
      - app/build/outputs/apk/release/battleships-release.apk

release:
  stage: release
  only:
    - tags

  script:
    - "curl \
      --header 'Content-Type: application/json' \
      --header 'JOB-TOKEN: $CI_JOB_TOKEN' \
      --data \"{ 
        \"name\": \"BattleShips Release $CI_COMMIT_REF_NAME\",
        \"tag_name\": \"$CI_COMMIT_REF_NAME\",
        \"description\": \"$CI_COMMIT_MESSAGE\",
        \"assets\": {
          \"links\": [
            {
              \"name\": \"battleships.apk\",
              \"url\": \"https://gitlab.com/api/v4/projects/$CI_PROJECT_ID/jobs/$CI_JOB_ID/artifacts\"
            }
          ]
        }
      }\"
      --request POST https://gitlab.com/api/v4/projects/$CI_PROJECT_ID/releases"