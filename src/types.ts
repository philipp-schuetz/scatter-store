interface GoogleDriveProvider extends Provider {
}

interface Provider {
    id: string
    name: string
    type: string
}

interface Share {
    id: string
    name: string
    path: string
    providers: Provider[]
    lastUploaded: string
}

let p: Share = {
    id: "n",
    name: "n",
    path: "n",
    providers: [{id: "id", type: "gd", name: "GooDr"},{id: "id", type: "gd", name: "GooDr"}],
    lastUploaded: "n",
}