const app = Vue.createApp({
    data() {
        return {
            url: 'http://127.0.0.1:8080/api/clients/current',
            client: [],
            clientAccounts: [],
            hiddenButton: false,
            hiddenPersonal: true,
            hiddenThird: true,
            accountOrigin: '',
            accountDestination: '',
            clientAccountsFilters: [],
            amount: '',
            description: ''
        }
    },

    created() {
        this.loadData(this.url)
    },

    methods: {
        loadData(url) {
            axios.get(url).then(response => {
                this.client = response.data

                this.client.accounts.filter(account => {
                    this.clientAccounts.push(account.number)
                })

                this.clientAccounts = this.clientAccounts.sort()
            })
        },

        // removeItems() {
        //     // let filtered = arr.filter(val => {
        //     //     return val != value
        //     // })

        //     // console.log(filtered);

        //     // return filtered

        //     this.clientAccountsFilters = this.clientAccounts
        //     .filter(account => account != this.accountOrigin)
        // },

        transfer() {
            axios.post('/api/clients/current/transactions',
                `amount=${this.amount}&description=${this.description}&accountOrigin=${this.accountOrigin}&accountTarget=${this.accountDestination}`)
                .then(() => {
                    this.clear()

                    Swal.fire({
                        icon: 'success',
                        title: 'Great',
                        text: 'Succefully loan',
                    })
                })
                .catch(error => {
                    let errorData = error.response.data
                    Swal.fire({
                        icon: 'error',
                        title: 'Oops...',
                        text: `${errorData}`
                    })
                })
        },

        createTransfer() {
            Swal.fire({
                title: 'Make a transfer',
                text: "You won't be able to revert this!",
                icon: 'warning',
                showCancelButton: true,
                confirmButtonColor: '#3085d6',
                cancelButtonColor: '#d33',
                confirmButtonText: 'Yes, create it!'
            }).then((result) => {
                if (result.isConfirmed) {
                    this.transfer()
                }
            })
        },

        clear() {
            this.amount = ''
            this.description = ''
            this.accountOrigin = ''
            this.accountDestination = ''
        }
    },
}).mount('#app')