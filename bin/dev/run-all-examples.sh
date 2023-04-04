#!/bin/bash

set -e

export BASE_DIR=$(cd "$(dirname "$0")"; pwd)

export EXAMPLE_DIR="$BASE_DIR/../../kieker-examples"

start=$(pwd)
for example in $(find $EXAMPLE_DIR -name "executeExample.sh"); do
	echo "Executing $example"
	cd $(dirname "$example")
	./executeExample.sh
	cd $start
done

# end
