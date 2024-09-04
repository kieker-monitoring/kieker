#!/usr/bin/env sh

BIN_DIR=$(cd "$(dirname "$0")"; pwd)

BASE_DIR="${BIN_DIR}/../.."
KIEKER_VERSION="2.0.0-SNAPSHOT"

PACKAGE_NAME=architecture-recovery
BUILD_PATH="${BASE_DIR}/build/${PACKAGE_NAME}"
KIEKER_TOOLS="${BASE_DIR}/tools/"

if [ -d "${BUILD_PATH}" ] ; then
	rm -rf "{BUILD_PATH}"
fi

mkdir -p "${BUILD_PATH}"
cd "${BUILD_PATH}"

mkdir bin
mkdir lib

for I in allen-upper-limit cmi dar delta fxca maa mktable mop mt mvis sar relabel restructuring ; do
	unzip "${KIEKER_TOOLS}/$I/build/distributions/$I-$KIEKER_VERSION.zip"
	mv "$I-$KIEKER_VERSION/lib/"* lib/
	mv "$I-$KIEKER_VERSION/bin/"* bin/
	rm -rf "$I-$KIEKER_VERSION"
done

cd ..
tar -cvzpf "${BUILD_PATH}-${KIEKER_VERSION}.tgz" "${PACKAGE_NAME}"
zip -r "${BUILD_PATH}-${KIEKER_VERSION}.zip" "${PACKAGE_NAME}"

rm -rf "${BUILD_PATH}"

# end

