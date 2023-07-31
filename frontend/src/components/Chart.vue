<template>
  <div :style="{ width: '320px', height: '320px' }">
    <canvas id="myChart"></canvas>
  </div>
</template>

<script setup>
import Chart from 'chart.js/auto';
import { onMounted } from 'vue';

const props = defineProps({
  chartType: {
    type: String,
    required: true
  },
  chartData: {
    type: Object,
    required: true
  },
  chartOptions: {
    type: Object,
    required: true
  }
});

// Chart.defaults.color = '#BADA55';
Chart.defaults.borderColor = '#adbac7';

const ChartComponentConstructor = (chartType, chartData, chartOptions) => {
  const chartElement = document.getElementById('myChart');
  return new Chart(chartElement, {
    type: chartType,
    data: chartData,
    options: chartOptions
  });
};

onMounted(() => {
  const chart = ChartComponentConstructor(
    props.chartType,
    props.chartData,
    props.chartOptions
  );
});
</script>
