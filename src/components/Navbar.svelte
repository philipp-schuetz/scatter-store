<script>
    import {AdjustmentsVerticalSolid, ShareNodesSolid, InfoCircleSolid, ServerSolid} from "flowbite-svelte-icons";
    import {BottomNav, BottomNavItem} from "flowbite-svelte";
</script>

<nav>
    <BottomNav position="absolute" classInner="grid-cols-4">
        <BottomNavItem btnName="Shares">
            <ShareNodesSolid class="w-6 h-6 mb-1 text-gray-500 dark:text-gray-400 group-hover:text-primary-600 dark:group-hover:text-primary-500" />
        </BottomNavItem>
        <BottomNavItem btnName="Providers">
            <ServerSolid class="w-6 h-6 mb-1 text-gray-500 dark:text-gray-400 group-hover:text-primary-600 dark:group-hover:text-primary-500" />
        </BottomNavItem>
        <BottomNavItem btnName="Settings">
            <AdjustmentsVerticalSolid class="w-6 h-6 mb-1 text-gray-500 dark:text-gray-400 group-hover:text-primary-600 dark:group-hover:text-primary-500" />
        </BottomNavItem>
        <BottomNavItem btnName="About">
            <InfoCircleSolid class="w-6 h-6 mb-1 text-gray-500 dark:text-gray-400 group-hover:text-primary-600 dark:group-hover:text-primary-500" />
        </BottomNavItem>
    </BottomNav>
</nav>