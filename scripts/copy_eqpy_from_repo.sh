# Use wget to get eqpy from the repo

DIR=$( dirname $0 )
BACKUP_SUFFIX=1
wget --backups=$BACKUP_SUFFIX -i $DIR/eqpy_files.txt -P $DIR/../templates/subtmpl-eqpy
rm $DIR/../templates/subtmpl-eqpy/*.$BACKUP_SUFFIX
