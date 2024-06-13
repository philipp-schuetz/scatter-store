<script lang="ts">
    import {Input, Label, Helper, Button, Checkbox, A, MultiSelect, Fileupload} from "flowbite-svelte";
    import {goto} from "$app/navigation";
    import {page} from "$app/stores";

    const shareId = $page.params.share_id;

    let selectedProviders: string[] = [];
    const shareData = { // share data is fetched by the provided share id
        name: "Test Name",
        path: "/path/to/directory",
        providers: ["ftp", "aws"]
    }
    let shareDataEdited = shareData
    const providers = [
        {value: "google_drive", name: "Google Drive"},
        {value: "one_drive", name: "One Drive"},
        {value: "ftp", name: "FTP Server"},
        {value: "aws", name: "Amazon Webservices"},
        {value: "gcp", name: "Google Cloud Platform"}
    ];
    function reset() {
        shareDataEdited = shareData
    }
    function submit() {
        goto("/shares");
        // TODO send data to backend
    }
</script>

<form>
    <div class="mb-6">
        <Label for="first_name" class="mb-2">Share name</Label>
        <Input type="text" id="share_name" placeholder="Documents" bind:value={shareDataEdited.name} required/>
    </div>
    <div class="mb-6">
        <Label for="providers" class="mb-2">Providers</Label>
        <MultiSelect items={providers} id="providers" bind:value={shareDataEdited.providers} disabled/>
    </div>
    <div class="mb-6">
        <Label for="last_name" class="mb-2">Path</Label>
        <Input type="text" id="last_name" placeholder="/home/documents" bind:value={shareDataEdited.path} required disabled/>
    </div>
    <Button type="submit" on:click={submit}>Edit</Button>
    <Button on:click={reset}>Reset</Button>
</form>



