<script lang="ts">
    import {page} from '$app/stores';

    let shareId = $page.params.share_id;

    import {Input, Label, Helper, Button, Checkbox, A} from 'flowbite-svelte';
    import {MultiSelect} from 'flowbite-svelte';

    import {Fileupload} from 'flowbite-svelte';

    let value;

    let selected: string = [];
    let countries = [
        {value: 'google_drive', name: 'Google Drive'},
        {value: 'one_drive', name: 'One Drive'},
        {value: 'ftp', name: 'FTP Server'},
        {value: 'aws', name: 'Amazon Webservices'},
        {value: 'gcp', name: 'Google Cloud Platform'}
    ];
</script>

<form>

    <div class="mb-6">
        <Label for="first_name" class="mb-2">Share name</Label>
        <Input type="text" id="share_name" placeholder="Documents" required/>
    </div>
    <div class="mb-6">
        <Label for="last_name" class="mb-2">Path</Label>
        <Input type="text" id="last_name" placeholder="/home/documents" required/>
    </div>
    <div class="mb-6">
        <Label class="space-y-2 mb-2">
            <span>Upload file</span>
            <Fileupload bind:value/>
        </Label>
        <Label>File: {value}</Label>
    </div>
    <div class="mb-6">
        <Label for="providers" class="mb-2">Providers</Label>
        <MultiSelect items={countries} id="providers" bind:value={selected} size="md"/>
    </div>
    <Button type="submit">Add</Button>
</form>



