const app = Vue.createApp({
    data() {
        return {
            url: 'http://127.0.0.1:8080/api/clients/current',
            client: [],
            accounts: [],
            clientLoans: [],
        }
    },

    created() {
        this.loadData(this.url)
    },

    methods: {
        loadData(url) {
            axios.get(url).then(response => {
                this.client = response.data
                this.accounts = this.client.accounts

                this.accounts = this.accounts.sort((a, b) => a.id - b.id)

                this.clientLoans = this.client.clientLoan

                this.clientLoans = this.clientLoans.sort((a, b) => a.id - b.id)

                // if(this.accounts.length == 0)
                //     this.createAccount()
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

        formatDollars(number) {
            return new Intl.NumberFormat('en-US', { style: 'currency', currency: 'USD' }).format(number)
        },

        buttonCreate() {
            Swal.fire({
                title: 'Create Account',
                text: "You won't be able to revert this!",
                icon: 'warning',

                input: 'select',
                inputOptions: {
                    'SAVINGS': 'SAVINGS',
                    'CHECKING': 'CHECKING',
                },
                inputPlaceholder: 'Select Account Type',
                showCancelButton: true,
                confirmButtonColor: '#3085d6',
                cancelButtonColor: '#d33',
                confirmButtonText: 'Yes, create it!',

                inputValidator: function (value) {
                    return new Promise(function(resolve, reject) {
                        if(value !== ''){
                            resolve()
                        } else {
                            reject('Select an option')
                        }
                    })
                }
                
            }).then((result) => {
                if (result.value) {
                    this.createAccount(result.value)
                    Swal.fire(
                        'Created!',
                        'You have created an account.',
                        'success'
                    )
                }
            })
        },

        createAccount(str) {
            axios.post('/api/clients/current/accounts', `type=${str}`)
                .then(() => this.loadData(this.url))
                .catch(error => {
                    let errorData = error.response.data
                    Swal.fire({
                        icon: 'error',
                        title: 'Oops...',
                        text: `${errorData}`
                    })
                })
        },

        logout() {
            axios.post('/api/logout').then(() => window.location.assign('../home/index.html'))
                .catch(error => {
                    let errorData = error.response.data
                    Swal.fire({
                        icon: 'error',
                        title: 'Oops...',
                        text: `${errorData}`
                    })
                })
        },

        buttonDeleteAccount(str) {
            Swal.fire({
                title: 'Delete Account',
                text: "You won't be able to revert this!",
                icon: 'warning',
                showCancelButton: true,
                confirmButtonColor: '#3085d6',
                cancelButtonColor: '#d33',
                confirmButtonText: 'Yes, delete it!'
            }).then((result) => {
                if (result.isConfirmed) {
                    this.deleteAccount(str)
                    Swal.fire(
                        'Deleted!',
                        'You have deleted an account.',
                        'success'
                    )
                }
            })
        },

        deleteAccount(id) {
            axios.patch(`/api/clients/current/account/delete/${id}`)
                .then(() => this.loadData(this.url))
                .catch(error => {
                    let errorData = error.response.data
                    Swal.fire({
                        icon: 'error',
                        title: 'Oops...',
                        text: `${errorData}`
                    })
                })
        }
    },

}).mount('#app')