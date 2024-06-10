<script>
    import {AdjustmentsVerticalSolid, ShareNodesSolid, InfoCircleSolid, ServerSolid} from "flowbite-svelte-icons";
    import {BottomNav, BottomNavItem} from "flowbite-svelte";
    import { page } from '$app/stores';
    $: activeUrl = $page.url.pathname;
    let iconClass = "w-6 h-6 mb-1 text-gray-500 dark:text-gray-400 group-hover:text-primary-600 dark:group-hover:text-primary-500"
</script>

<nav>
    <BottomNav {activeUrl} position="absolute" classInner="grid-cols-4">
        <BottomNavItem btnName="Shares" href="/shares">
            <ShareNodesSolid class={iconClass} />
        </BottomNavItem>
        <BottomNavItem btnName="Providers" href="/providers">
            <ServerSolid class={iconClass}/>
        </BottomNavItem>
        <BottomNavItem btnName="Settings" href="/settings">
            <AdjustmentsVerticalSolid class={iconClass}/>
        </BottomNavItem>
        <BottomNavItem btnName="About" href="/about">
            <InfoCircleSolid class={iconClass}/>
        </BottomNavItem>
    </BottomNav>
</nav>