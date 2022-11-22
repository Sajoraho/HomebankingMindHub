const app = Vue.createApp({
    data(){
        return{
            url:'http://127.0.0.1:8080/api/clients/current',
            client: [],
            clientTransactions: {},
            transactions: [],
            number: '',
            balance: '',
            date: '',
        }
    },

    created(){
        this.loadData(this.url)
    },

    mounted() {
        queryString = location.search
        params = new URLSearchParams(queryString)
        id = params.get('id')
    },

    methods: {
        loadData(url){
            axios.get(url).then(response=>{
                this.client = response.data;

                this.clientTransactions = this.client.accounts.find(element => element.id == id)

                this.number = this.clientTransactions.number
                this.balance = this.clientTransactions.balance
                this.date = this.clientTransactions.creationDate
                this.transactions = this.clientTransactions.transactions

                this.transactions = this.transactions.sort((a, b) => a.id - b.id)

                console.log( this.transactions[0].type);
            })
            .catch(error => console.log(error));
        },

        formatDollars(number){
            return new Intl.NumberFormat('en-US',{style: 'currency', currency: 'USD'}).format(number)
        },

        logout(){
            axios.post('/api/logout').then(() => window.location.assign('../home/index.html'))
        }

    },

    computed: {
        // color(type) {
        //     if (type == 'CREDIT')
        //         return {"table-success": true}
        // }
    }
}).mount('#app')