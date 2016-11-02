#! /usr/bin/env bash

# Use wget to get eqr from the repo

DIR=$( dirname $0 )
BACKUP_SUFFIX=1
wget --backups=$BACKUP_SUFFIX -i $DIR/eqr_files.txt -P $DIR/../templates/subtmpl-eqr/eqr
rm $DIR/../templates/subtmpl-eqr/eqr/*.$BACKUP_SUFFIX
chmod a+x $DIR/../templates/subtmpl-eqr/eqr/bootstrap
