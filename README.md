# Scatter Store
A tool to safely store files using common online storage providers.

## Idea
Store files using common online storage providers.
Before uploading, the files are encrypted and split into multiple parts.
Each file part is then uploaded to a different storage provider.

## Definitions
### Share
A Share is a named collection of files grouped by a directory path. The files contained
in the collection are uploaded together and their current status is also tracked together.
File statuses are used to check if a file was modified, since it was last uploaded.

Properties:
- id: string/uuid
- name: string (name of the share displayed in the ui)
- path: string (root directory containing the files to upload)
- providers: list (list of providers)
- last_uploaded: string (datetime string of the last upload)
- hash: string (hash over all files, used for change detection)

### Provider
A Provider is the online storage used for file uploads. Both open and closed/commercial
products should be supported. Examples are: (S)FTP, One Drive, Google Cloud Platform.

Properties:
- id: string/uuid
- name: string (name of the share displayed in the ui)
- type: string (identify different provider types; e.g. ftp, od, gcp)

### Settings

Properties:
- databaseEncryption: bool (default: false)
- databaseLocation: string (path to the database file; could be used in portable setup)
- advancedMode: bool (maybe?; allow changing share paths; this mode will break things)