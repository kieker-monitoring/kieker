#!/usr/bin/env sh

BIN_DIR=$(cd "$(dirname "$0")"; pwd)

BASE_DIR="${BIN_DIR}/../.."
KIEKER_VERSION="2.0.0-SNAPSHOT"

PACKAGE_NAME=trace-analysis
BUILD_PATH="${BASE_DIR}/build/${PACKAGE_NAME}"
KIEKER_TOOLS="${BASE_DIR}/tools/"

if [ -d "${BUILD_PATH}" ] ; then
	rm -rf "{BUILD_PATH}"
fi

mkdir -p "${BUILD_PATH}"
cd "${BUILD_PATH}"

mkdir bin
mkdir lib

for I in trace-analysis trace-analysis-gui ; do
	unzip "${KIEKER_TOOLS}/$I/build/distributions/$I-$VERSION.zip"
	mv "$I-$VERSION/lib/"* lib/
	mv "$I-$VERSION/bin/"* bin/
	rm -rf "$I-$VERSION"
done

cd ..
tar -cvzpf "${BUILD_PATH}-${VERSION}.tgz" "${PACKAGE_NAME}"
zip -r "${BUILD_PATH}-${VERSION}.zip" "${PACKAGE_NAME}"

rm -rf "${BUILD_PATH}"

# end

