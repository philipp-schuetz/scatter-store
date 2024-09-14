<script lang="ts">
  import { invoke } from "@tauri-apps/api/tauri";
  import { goto } from '$app/navigation';
  import { onMount } from 'svelte';

  let name = "";
  let greetMsg = "";

  onMount(() => {
      goto('/shares');
  });

  async function greet() {
    // Learn more about Tauri commands at https://tauri.app/v1/guides/features/command
    greetMsg = await invoke("greet", { name });
  }
</script>

<div class="container">

</div>
