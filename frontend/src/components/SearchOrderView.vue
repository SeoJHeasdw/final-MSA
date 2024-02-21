<template>

    <v-data-table
        :headers="headers"
        :items="searchOrder"
        :items-per-page="5"
        class="elevation-1"
    ></v-data-table>

</template>

<script>
    const axios = require('axios').default;

    export default {
        name: 'SearchOrderView',
        props: {
            value: Object,
            editMode: Boolean,
            isNew: Boolean
        },
        data: () => ({
            headers: [
                { text: "id", value: "id" },
            ],
            searchOrder : [],
        }),
          async created() {
            var temp = await axios.get(axios.fixUrl('/searchOrders'))

            temp.data._embedded.searchOrders.map(obj => obj.id=obj._links.self.href.split("/")[obj._links.self.href.split("/").length - 1])

            this.searchOrder = temp.data._embedded.searchOrders;
        },
        methods: {
        }
    }
</script>

