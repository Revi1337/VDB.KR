<template>
  <q-page class="flex flex-center">
    <div :style="canvasStyle">
      <canvas id="myChart4"></canvas>
    </div>
    <div id="legend-container" class="q-ml-xl"></div>
  </q-page>
</template>

<script setup>
import { onMounted, onUpdated, ref } from 'vue';
import { useRouter } from 'vue-router';
import { searchTotalCveByEachYear } from 'src/api/statistics';
import { Chart, registerables } from 'chart.js';

Chart.register(...registerables);
const canvasStyle = ref(['width: 300px', 'height: 300px']);
const router = useRouter();

onMounted(() => {
  fetchSearchTotalCveByEachYear();
});

onUpdated(() => {});

const isDoughnutLoaded = ref(false);
const doughnutData = ref({});
const totalYear = ref([]);
const fetchSearchTotalCveByEachYear = async () => {
  try {
    let yearArray = [];
    let totalCountArray = [];
    isDoughnutLoaded.value = false;
    console.log('start fetch data');
    const { data } = await searchTotalCveByEachYear();
    totalYear.value = data.payload;

    for (const entry of totalYear.value) {
      yearArray.push(entry.year);
      totalCountArray.push(entry.totalCount);
    }

    doughnutData.value = {
      labels: yearArray,
      datasets: [
        {
          label: 'Published CVE',
          data: totalCountArray,
          fill: true,
          backgroundColor: [
            'rgb(189, 99, 91)',
            'rgb(212, 207, 72)',
            'rgb(90, 212, 53)',
            'rgb(63, 186, 106)',
            'rgb(48, 194, 165)',
            'rgb(44, 131, 201)',
            'rgb(31, 60, 191)',
            'rgb(204, 37, 219)',
            'rgb(219, 37, 143)',
            'rgb(219, 134, 138)',
            'rgb(154, 136, 179)',
            'rgb(98, 163, 115)',
            'rgb(127, 172, 212)',
            'rgb(255, 205, 86)'
          ],
          hoverOffset: 4
        }
      ]
    };

    console.log('end fetch data');
    isDoughnutLoaded.value = true;

    console.log('inject chart');
    createChartInstance('myChart4', 'doughnut', doughnutData.value);
  } catch (error) {
    console.error(error);
  }
};

const createChartInstance = (elementId, chartType, data) => {
  const doughnutLabel = {
    id: 'doughnutLabel',
    beforeDatasetsDraw(chart, args, pluginOptions) {
      const { ctx, data } = chart;
      ctx.save();
      const xCoor = chart.getDatasetMeta(0).data[0].x;
      const yCoor = chart.getDatasetMeta(0).data[0].y;
      ctx.font = 'bold 18px sans-serif';
      // ctx.fillStyle = 'rgba(98, 163, 115, 1)';
      ctx.textAlign = 'center';
      ctx.textBaseline = 'middle';
      const totalVulnerabilities = data.datasets[0].data.reduce(
        (accumulator, currentValue) => accumulator + currentValue,
        0
      );
      ctx.fillText(totalVulnerabilities, xCoor, yCoor);
      // ctx.fillText(
      //   `${data.labels[0]} : ${data.datasets[0].data[0]}`,
      //   xCoor,
      //   yCoor
      // );
    }
  };

  const chart = new Chart(document.getElementById(elementId), {
    type: chartType,
    data: data,
    plugins: [doughnutLabel, htmlLegendPlugin],
    options: {
      cutoutPercentage: 55,
      plugins: {
        htmlLegend: {
          containerID: 'legend-container'
        },
        legend: {
          display: false
        }
      },
      onClick: function (evt) {
        const points = chart.getElementsAtEventForMode(
          evt,
          'nearest',
          { intersect: true },
          true
        );

        if (points.length) {
          const firstPoint = points[0];
          console.log(firstPoint);
          let label = chart.data.labels[firstPoint.index];
          let value =
            chart.data.datasets[firstPoint.datasetIndex].data[firstPoint.index];
          bindingClickEvent(label);
        }
      }
    }
  });
};

const bindingClickEvent = label => {
  router.push({
    name: 'YearStatistic',
    params: {
      year: label
    }
  });
};

const getOrCreateLegendList = (chart, id) => {
  // LegendContainer
  const legendContainer = document.getElementById(id);
  let listContainer = legendContainer.querySelector('#legend-container > div');
  if (!listContainer) {
    listContainer = document.createElement('div');
    listContainer.classList.add('row');
    listContainer.classList.add('items-center');
    listContainer.style.width = '510px';
    listContainer.style.height = '300px';
    listContainer.style.margin = 0;
    listContainer.style.padding = 0;
    legendContainer.appendChild(listContainer);
  }
  return listContainer;
};

const htmlLegendPlugin = {
  id: 'htmlLegend',
  afterUpdate(chart, args, options) {
    const ul = getOrCreateLegendList(chart, options.containerID);

    while (ul.firstChild) {
      ul.firstChild.remove();
    }

    const datasets = chart.data.datasets;
    const items = chart.options.plugins.legend.labels.generateLabels(chart);

    items.forEach((item, index) => {
      const li = document.createElement('div');
      li.style.cursor = 'pointer';
      li.style.width = '150px';
      li.style.marginRight = '20px';

      li.onclick = () => {
        const { type } = chart.config;
        if (type === 'pie' || type === 'doughnut') {
          // Pie and doughnut charts only have a single dataset and visibility is per item
          chart.toggleDataVisibility(item.index);
        } else {
          chart.setDatasetVisibility(
            item.datasetIndex,
            !chart.isDatasetVisible(item.datasetIndex)
          );
        }
        chart.update();
      };

      // Text Prefix
      const textPrefix = document.createElement('span');
      const prefix = document.createTextNode(`In Year ${item.text}`);
      textPrefix.style.fontWeight = '500';
      textPrefix.style.color = '#006192';
      textPrefix.style.margin = 0;
      textPrefix.style.padding = 0;
      textPrefix.style.textDecoration = item.hidden ? 'line-through' : '';
      textPrefix.appendChild(prefix);
      // Color box
      const boxSpan = document.createElement('span');
      boxSpan.style.background = item.fillStyle;
      boxSpan.style.borderColor = item.strokeStyle;
      boxSpan.style.borderWidth = item.lineWidth + 'px';
      boxSpan.style.display = 'inline-block';
      boxSpan.style.flexShrink = 0;
      boxSpan.style.height = '20px';
      boxSpan.style.marginLeft = '45px';
      boxSpan.style.borderRadius = '10px';
      boxSpan.style.width = '20px';
      // Year Container
      const yearContainer = document.createElement('div');

      yearContainer.classList.add('row');
      yearContainer.classList.add('items-center');
      yearContainer.classList.add('justify-around');
      yearContainer.appendChild(textPrefix);
      yearContainer.appendChild(boxSpan);

      // Text
      const vulnerabilitiesCount = document.createElement('span');
      vulnerabilitiesCount.style.color = item.fontColor;
      vulnerabilitiesCount.style.fontWeight = 600;
      vulnerabilitiesCount.style.padding = 0;
      vulnerabilitiesCount.style.textDecoration = item.hidden
        ? 'line-through'
        : '';

      const text = document.createTextNode(datasets[0].data[index]);
      vulnerabilitiesCount.appendChild(text);
      // Text Postfix
      const textPostfix = document.createElement('span');
      const postfix = document.createTextNode('Vulnerabilities');
      textPostfix.style.color = '#006192';
      textPostfix.style.margin = 0;
      textPostfix.style.padding = 0;
      textPostfix.style.textDecoration = item.hidden ? 'line-through' : '';
      textPostfix.classList.add('items-center');
      textPostfix.appendChild(postfix);
      // Text Container
      const textContainer = document.createElement('div');
      textContainer.classList.add('col-4');
      textContainer.classList.add('row');
      textContainer.classList.add('items-center');
      textContainer.classList.add('justify-between');
      textContainer.appendChild(vulnerabilitiesCount);
      textContainer.appendChild(textPostfix);

      li.appendChild(yearContainer);
      li.appendChild(textContainer);

      ul.appendChild(li);
    });
    let valueDisplays = document.querySelectorAll('.num');
    let interval = 10;
    console.log('increased!!');

    valueDisplays.forEach(valueDisplay => {
      let startValue = 0;
      let endValue = parseInt(valueDisplay.getAttribute('data-val'));
      let duration = Math.floor(interval / endValue);
      let counter = setInterval(function () {
        startValue += 40;
        valueDisplay.textContent = startValue;
        if (startValue >= endValue) {
          clearInterval(counter);
        }
      }, duration);
    });
  }
};
</script>

<!-- https://www.youtube.com/watch?v=c2mzQKpd_DI -->
<!-- https://vue-chart-3.netlify.app/guide/usage/chart-instance.html -->
<!-- https://www.chartjs.org/docs/master/getting-started/integration.html#bundlers-webpack-rollup-etc -->
