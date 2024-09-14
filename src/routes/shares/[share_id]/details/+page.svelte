<script lang="ts">
    import {Input, Label, Button, MultiSelect} from "flowbite-svelte";
    import {goto} from "$app/navigation";
    import {page} from "$app/stores";

    const shareId = $page.params.share_id;

    const shareData = { // share data is fetched by the provided share id
        name: "Test Name",
        path: "/path/to/directory",
        providers: ["ftp", "aws"]
    }
    const providers = [
        {value: "google_drive", name: "Google Drive"},
        {value: "one_drive", name: "One Drive"},
        {value: "ftp", name: "FTP Server"},
        {value: "aws", name: "Amazon Webservices"},
        {value: "gcp", name: "Google Cloud Platform"}
    ];

    function back() {
        goto("/shares");
    }

    function deleteShare() {
        goto("/shares");
        // TODO delete db entry; also delete remote data?
    }
</script>

Name: {shareData.name}<br>
Providers: {shareData.providers}<br>
Directory: {shareData.path}<br>

<Button on:click={back}>Back</Button>
<Button on:click={deleteShare}>Delete</Button>

