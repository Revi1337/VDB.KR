<template>
  <q-page class="flex flex-center">
    <AppCard :style="aboutCard">
      <div class="about q-mt-lg">
        <div class="text-center text-overline">WHAT IS SYNK</div>
        <div class="text-center q-mt-md q-mb-xl">
          Find and fix vulnerabilities in 5 minutes
        </div>
      </div>

      <div>
        <div class="row items-center justify-center q-px-lg">
          <template v-for="(value, index) in 3" :key="index">
            <AppCard :style="aboutMiddle" class="q-mx-sm col flex flex-center">
              <div>asdasd</div>
              <div>asdasd</div>
            </AppCard>
          </template>
        </div>

        <div class="q-mt-xl row items-center justify-center">
          <template v-for="(value, index) in 4" :key="index">
            <AppCard
              :style="aboutFooter"
              class="binding-event col q-mx-sm q-mx-md flex flex-center"
              @mouseenter="toggleMouseEvents"
              @mouseleave="toggleMouseEvents"
            >
              <div>
                <div>one</div>
                <div style="border: 1px solid red; border-radius: 5px">
                  <q-icon name="fa-solid fa-arrow-right" size="xs"></q-icon>
                </div>
              </div>
            </AppCard>
          </template>
        </div>
      </div>
    </AppCard>
  </q-page>
</template>

<script setup>
import AppCard from 'src/components/AppCard.vue';

const toggleMouseEvents = event => event.target.classList.toggle('hover');

const aboutCard = {
  backgroundColor: '#f6f4fa',
  height: '720px',
  width: '95%',
  padding: '15px'
};

const aboutMiddle = {
  // backgroundColor: '#transparent',
  backgroundColor: '#FFF',
  height: '220px',
  width: '320px'
};

const aboutFooter = {
  backgroundColor: '#FFF',
  height: '200px',
  width: '200px'
};
</script>

<style lang="scss" scoped>
* {
  color: #030328;
}

.about {
  :nth-child(1) {
    color: #441c99;
    font-weight: 670;
    font-size: 16px;
  }
  :nth-child(2) {
    font-size: 40px;
    font-weight: 800;
  }
}

.binding-event {
  transition: all 0.2s ease-in-out;
  &:hover {
    box-shadow: 0 0 5px $primary;
    background-color: rgba(70, 157, 242, 0.05) !important;
  }
}
</style>
