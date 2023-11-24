<script setup lang="ts"></script>

<template>
  <div class="q-pa-md">
    <q-toolbar class="q-my-md shadow-2">
      <q-btn flat round dense icon="menu" class="q-mr-sm" />
      <q-separator dark vertical inset />
      <q-btn stretch flat label="首页" />

      <q-space />

      <q-btn-dropdown stretch flat label="分类">
        <q-list>
          <q-item
            clickable
            v-close-popup
            tabindex="0"
          >
            <q-item-section>
              <q-item-label>Photos</q-item-label>
            </q-item-section>
          </q-item>
        </q-list>
      </q-btn-dropdown>
      <q-separator dark vertical />
      <q-btn stretch flat label="Link" />
      <q-separator dark vertical />
      <q-btn stretch flat label="Link" />
    </q-toolbar>
  </div>
</template>

<style scoped></style>
