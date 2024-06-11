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
        <TableBodyRow>
            <TableBodyCell>Documents</TableBodyCell>
            <TableBodyCell>/home</TableBodyCell>
            <TableBodyCell>01/01/2001 00:00</TableBodyCell>
            <TableBodyCell>
                <ButtonGroup>
                    <Button href="/">
                        <CloudArrowUpSolid class="w-4 h-4 me-2"/>
                        Upload
                    </Button>
                    <Button href="/">
                        <EditSolid class="w-4 h-4 me-2"/>
                        Edit
                    </Button>
                </ButtonGroup>
            </TableBodyCell>
        </TableBodyRow>
        <TableBodyRow>
            <TableBodyCell>Documents</TableBodyCell>
            <TableBodyCell>/home</TableBodyCell>
            <TableBodyCell>01/01/2001 00:00</TableBodyCell>
            <TableBodyCell>
                <ButtonGroup>
                    <Button href="/">
                        <CloudArrowUpSolid class="w-4 h-4 me-2"/>
                        Upload
                    </Button>
                    <Button href="/">
                        <EditSolid class="w-4 h-4 me-2"/>
                        Edit
                    </Button>
                </ButtonGroup>
            </TableBodyCell>
        </TableBodyRow>
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