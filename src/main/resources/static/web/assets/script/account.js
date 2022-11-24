const app = Vue.createApp({
    data() {
        return {
            url: 'http://127.0.0.1:8080/api/clients/current',
            client: [],
            clientTransactions: {},
            transactions: [],
            number: '',
            balance: '',
            date: '',
            type: ''
        }
    },

    created() {
        this.loadData(this.url)
    },

    mounted() {
        queryString = location.search
        params = new URLSearchParams(queryString)
        id = params.get('id')
    },

    methods: {
        loadData(url) {
            axios.get(url).then(response => {
                this.client = response.data;

                this.clientTransactions = this.client.accounts.find(element => element.id == id)

                this.number = this.clientTransactions.number
                this.balance = this.clientTransactions.balance
                this.date = this.clientTransactions.creationDate
                this.type = this.clientTransactions.type
                this.transactions = this.clientTransactions.transactions

                this.transactions = this.transactions.sort((a, b) => a.id - b.id)

                console.log(this.transactions[0].type);
            })
                .catch(error => console.log(error));
        },

        formatDollars(number) {
            return new Intl.NumberFormat('en-US', { style: 'currency', currency: 'USD' }).format(number)
        },

        logout() {
            axios.post('/api/logout').then(() => window.location.assign('../home/index.html'))
        },

        buttonPDF() {
            Swal.fire({
                title: 'Download Account Statement',
                text: "Do you want to download the PDF?",
                icon: 'warning',
                showCancelButton: true,
                confirmButtonColor: '#3085d6',
                cancelButtonColor: '#d33',
                confirmButtonText: 'Yes, download it!'
            }).then((result) => {
                if (result.isConfirmed) {
                    this.downloadPDF()
                    Swal.fire(
                        'Downloaded!',
                        'You have downloaded the account statement.',
                        'success'
                    )
                }
            })
        },

        downloadPDF() {
            axios.post('/api/pdf', `number=${this.number}&localDateMinus=$'month'`)
                .then(() => window.location.href = '/api/pdf')
        }
    },

    computed: {
        // color(type) {
        //     if (type == 'CREDIT')
        //         return {"table-success": true}
        // }
    }
}).mount('#app')