interface GoogleDriveProvider extends Provider {
}

interface Provider {
    id: string
    name: string
    displayName: string
}

interface Share {
    id: string
    name: string
    directory: string
    providers: Provider[]
    lastUploaded: string
}

let p: Share = {
    id: "n",
    name: "n",
    directory: "n",
    providers: [{id: "id", name: "gd", displayName: "GooDr"},{id: "id", name: "gd", displayName: "GooDr"}],
    lastUploaded: "n",
}