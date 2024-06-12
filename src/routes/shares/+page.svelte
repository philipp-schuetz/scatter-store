<script>
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
    import {CloudArrowUpSolid, EditSolid, CirclePlusSolid} from 'flowbite-svelte-icons';

    const shareData = [
        {
            id: "1234ger34g43QW",
            name: "Documents",
            path: "/documents",
            lastUploaded: "01/01/2000 00:00"
        },
        {
            id: "fga4a4as4ga4a4g",
            name: "Photos",
            path: "/photos",
            lastUploaded: "01/01/2001 00:01"
        },
        {
            id: "a4gg44g4g8989",
            name: "Music",
            path: "/music",
            lastUploaded: "01/01/2002 00:02"
        }
    ]

</script>

<Table striped={true} shadow>
    <TableHead>
        <TableHeadCell>Name</TableHeadCell>
        <TableHeadCell>Path</TableHeadCell>
        <TableHeadCell>Last Uploaded</TableHeadCell>
        <TableHeadCell>
            <span class="sr-only">Actions</span>
        </TableHeadCell>
    </TableHead>
    <TableBody tableBodyClass="divide-y">
        {#each shareData as data}
            <TableBodyRow>
                <TableBodyCell>{data.name}</TableBodyCell>
                <TableBodyCell>{data.path}</TableBodyCell>
                <TableBodyCell>{data.lastUploaded}</TableBodyCell>
                <TableBodyCell>
                    <ButtonGroup>
                        <Button href="/">
                            <CloudArrowUpSolid class="w-4 h-4 me-2"/>
                            Upload
                        </Button>
                        <Button href="/shares/{data.id}/edit">
                            <EditSolid class="w-4 h-4 me-2"/>
                            Edit
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
                    <Button href="/">
                        <CirclePlusSolid class="w-4 h-4 me-2"/>
                        Create a share
                    </Button>
                </ButtonGroup>
            </TableBodyCell>
        </TableBodyRow>
    </TableBody>
</Table>