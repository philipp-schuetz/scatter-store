<script lang="ts">
    import {
        Table,
        TableBody,
        TableBodyCell,
        TableBodyRow,
        TableHead,
        TableHeadCell,
        ButtonGroup,
        Button
    } from 'flowbite-svelte';
    import {CloudArrowUpOutline, ListOutline, CirclePlusSolid} from 'flowbite-svelte-icons';

    const providerData: Provider[] = [
        {
            id: "1234ger34g43QW",
            type: "ftp",
            name: "FTP Server 1",
        },
        {
            id: "pq93gh8q384gnf",
            type: "google_drive",
            name: "Google Drive Main",
        },
        {
            id: "qo34bg89qqg",
            type: "gcp",
            name: "Google Cloud Platform",
        },
    ]

</script>

<Table striped={true} shadow>
    <TableHead>
        <TableHeadCell>Name</TableHeadCell>
        <TableHeadCell>
            <span class="sr-only">Actions</span>
        </TableHeadCell>
    </TableHead>
    <TableBody tableBodyClass="divide-y">
        {#each providerData as data}
            <TableBodyRow>
                <TableBodyCell>{data.name}</TableBodyCell>
                <TableBodyCell>
                    <ButtonGroup>
                        <Button href="/providers/{data.id}/details">
                            <ListOutline class="w-4 h-4 me-2"/>
                            Details
                        </Button>
                    </ButtonGroup>
                </TableBodyCell>
            </TableBodyRow>
        {/each}
        <TableBodyRow>
            <TableBodyCell></TableBodyCell>
            <TableBodyCell></TableBodyCell>
            <TableBodyCell></TableBodyCell>
            <TableBodyCell>
                <ButtonGroup>
                    <Button href="/providers/create">
                        <CirclePlusSolid class="w-4 h-4 me-2"/>
                        Create a provider
                    </Button>
                </ButtonGroup>
            </TableBodyCell>
        </TableBodyRow>
    </TableBody>
</Table>