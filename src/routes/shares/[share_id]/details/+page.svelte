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
    let shareDataEdited = {...shareData};
    const providers = [
        {value: "google_drive", name: "Google Drive"},
        {value: "one_drive", name: "One Drive"},
        {value: "ftp", name: "FTP Server"},
        {value: "aws", name: "Amazon Webservices"},
        {value: "gcp", name: "Google Cloud Platform"}
    ];
    function resetValue() {
        shareDataEdited = shareData
    }
    function submit() {
        goto("/shares");
        // TODO send data to backend
    }
    function deleteShare() {
        goto("/shares");
        // TODO delete db entry; also delete remote data?
    }
</script>

<form>
    <div class="mb-6">
        <Label for="name" class="mb-2">Share name</Label>
        <Input type="text" id="name" placeholder="" bind:value={shareDataEdited.name} required/>
    </div>
    <div class="mb-6">
        <Label for="providers" class="mb-2">Providers</Label>
        <MultiSelect items={providers} id="providers" bind:value={shareDataEdited.providers} disabled/>
    </div>
    <div class="mb-6">
        <Label for="directory" class="mb-2">Path</Label>
        <Input type="text" id="directory" placeholder="" bind:value={shareDataEdited.path} disabled/>
    </div>
    <Button type="submit" on:submit={submit}>Edit</Button>
    <Button on:click={resetValue}>Reset</Button>
    <Button on:click={deleteShare}>Delete</Button>
</form>



