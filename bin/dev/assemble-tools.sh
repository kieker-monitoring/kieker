#!/usr/bin/env sh

BIN_DIR=$(cd "$(dirname "$0")"; pwd)

BASE_DIR="${BIN_DIR}/../.."
VERSION="2.0.0-SNAPSHOT"

PACKAGE_NAME=architecture-recovery
BUILD_PATH="${BASE_DIR}/build/${PACKAGE_NAME}"
KIEKER_TOOLS="${BASE_DIR}/kieker-tools/"

if [ -d "${BUILD_PATH}" ] ; then
	rm -rf "{BUILD_PATH}"
fi

mkdir -p "${BUILD_PATH}"
cd "${BUILD_PATH}"

mkdir bin
mkdir lib

for I in allen-upper-limit cmi dar delta fxca maa mktable mop mt mvis sar relabel restructuring ; do
	unzip "${KIEKER_TOOLS}/$I/build/distributions/$I-$VERSION.zip"
	mv "$I-$VERSION/lib/"* lib/
	mv "$I-$VERSION/bin/"* bin/
	rm -rf "$I-$VERSION"
done

cd ..
tar -cvzpf "${BUILD_PATH}-$VERSION.tgz" "${PACKAGE_NAME}"
rm -rf "${BUILD_PATH}"

# end

