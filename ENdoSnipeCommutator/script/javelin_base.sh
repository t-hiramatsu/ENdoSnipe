#! /bin/sh

if [ "$APP_NAME" == "" ]; then
    exit 2;
fi

BASE_PATH=$(readlink -f $(dirname $0))/..

stap -g ${BASE_PATH}/stap/javelin_${APP_NAME}.stp | ${BASE_PATH}/ens_commutator ${BASE_PATH}/conf/commutator.properties
