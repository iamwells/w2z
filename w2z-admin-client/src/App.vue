<script setup lang="ts"></script>

<template>
  <Transition>
    <RouterView v-slot="{ Component }" :key="$route.path">
      <component :is="Component" />
    </RouterView>
  </Transition>
</template>

<style scoped></style>
