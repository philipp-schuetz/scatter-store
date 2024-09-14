<script lang="ts">
    import {Input, Label, Button, MultiSelect} from 'flowbite-svelte';
    import {goto} from "$app/navigation";
    import {open} from '@tauri-apps/api/dialog';

    let selected: string[] = [];
    let providers = [
        {value: 'google_drive', name: 'Google Drive'},
        {value: 'one_drive', name: 'One Drive'},
        {value: 'ftp', name: 'FTP Server'},
        {value: 'aws', name: 'Amazon Webservices'},
        {value: 'gcp', name: 'Google Cloud Platform'}
    ];
    let directory: string;

    function submit() {
        goto('/shares');
    }

    async function directoryDialog() {
        const selected = await open({
            directory: true,
            multiple: false,
        });

        if (!Array.isArray(selected) && selected !== null) {
            directory = selected;
        }
        return;
    }
</script>

<form>
    <div class="mb-6">
        <Label for="first_name" class="mb-2">Share name</Label>
        <Input type="text" id="share_name" placeholder="" required/>
    </div>
    <div class="mb-6">
        <Label for="providers" class="mb-2">Providers</Label>
        <MultiSelect items={providers} id="providers" bind:value={selected} size="md"/>
    </div>
    <div class="mb-6">
        <Label for="last_name" class="mb-2">Directory</Label>
        <Input type="text" id="last_name" placeholder="" bind:value={directory} readonly required/>
        <Button on:click={directoryDialog}>Select Directory</Button>
    </div>
    <Button type="submit" on:submit={submit}>Create</Button>
</form>



