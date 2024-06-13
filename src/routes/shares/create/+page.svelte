<script lang="ts">
    import {Input, Label, Helper, Button, Checkbox, A, MultiSelect, Fileupload} from 'flowbite-svelte';
    import {goto} from "$app/navigation";

    let value;
    let selected: string = [];
    let providers = [
        {value: 'google_drive', name: 'Google Drive'},
        {value: 'one_drive', name: 'One Drive'},
        {value: 'ftp', name: 'FTP Server'},
        {value: 'aws', name: 'Amazon Webservices'},
        {value: 'gcp', name: 'Google Cloud Platform'}
    ];
    function submit() {
        goto('/shares');
    }
</script>

<form>
    <div class="mb-6">
        <Label for="first_name" class="mb-2">Share name</Label>
        <Input type="text" id="share_name" placeholder="Documents" required/>
    </div>
    <div class="mb-6">
        <Label for="providers" class="mb-2">Providers</Label>
        <MultiSelect items={providers} id="providers" bind:value={selected} size="md"/>
    </div>
    <div class="mb-6">
        <Label for="last_name" class="mb-2">Path</Label>
        <Input type="text" id="last_name" placeholder="/home/documents" required/>
    </div>
    <Button type="submit" on:click={submit}>Create</Button>
</form>



