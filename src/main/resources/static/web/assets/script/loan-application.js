const app = Vue.createApp({
    data() {
        return {
            url: 'http://127.0.0.1:8080/api/clients/current',
            client: [],
            loans: [],
            nameLoan: '',
            loanSelected: [],
            payments: [],
            clientAccounts: [],
            maxAmount: '',
            id: '',
            amount: '',
            payment: '',
            numberAccount: '',
            monthly: '',
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
                .catch(error => {
                    let errorData = error.response.data
                    Swal.fire({
                        icon: 'error',
                        title: 'Oops...',
                        text: `${errorData}`
                    })
                })

            axios.get('/api/loans').then(response => {
                this.loans = response.data

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

        createLoan() {
            axios.post('/api/clients/current/loans',
                {
                    "id": this.id,
                    "amount": this.amount,
                    "payment": this.payment,
                    "numberAccount": this.numberAccount
                })
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

        createButton() {
            Swal.fire({
                title: 'Make a loan',
                text: "You won't be able to revert this!",
                icon: 'warning',
                showCancelButton: true,
                confirmButtonColor: '#3085d6',
                cancelButtonColor: '#d33',
                confirmButtonText: 'Yes, create it!'
            }).then((result) => {
                if (result.isConfirmed) {
                    this.createLoan()
                }
            })
        },

        clear() {
            this.id = ''
            this.amount = ''
            this.payment = ''
            this.numberAccount = ''
            this.monthly = ''
        }
    },

    computed: {
        selectId() {
            if (this.nameLoan !== '') {

                this.loanSelected = this.loans.filter(element => {
                    return element.name == this.nameLoan
                })

                this.id = this.loanSelected[0].id
                this.maxAmount = this.loanSelected[0].maxAmount

                this.loanSelected.forEach(loan => {
                    this.payments = loan.payments
                })

                if(this.amount !== '' && this.payment !== '') {

                    this.monthly = (this.amount * 1.20 )/ this.payment
                }

            }
        },
    }

}).mount('#app')