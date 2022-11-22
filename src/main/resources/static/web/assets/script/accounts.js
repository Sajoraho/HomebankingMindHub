const app = Vue.createApp({
    data(){
        return{
            url:'http://127.0.0.1:8080/api/clients/current',
            client: [],
            accounts: [],
            clientLoans: [], 
        }
    },

    created() {
        this.loadData(this.url)
    },

    methods: {
        loadData(url){
            axios.get(url).then(response =>{
                this.client = response.data
                this.accounts = this.client.accounts

                this.accounts = this.accounts.sort((a, b) => a.id - b.id)

                this.clientLoans = this.client.clientLoan

                this.clientLoans = this.clientLoans.sort((a, b)=> a.id - b.id)

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

        formatDollars(number){
            return new Intl.NumberFormat('en-US',{style: 'currency', currency: 'USD'}).format(number)
        },

        buttonCreate(){
            Swal.fire({
                title: 'Create Account',
                text: "You won't be able to revert this!",
                icon: 'warning',
                showCancelButton: true,
                confirmButtonColor: '#3085d6',
                cancelButtonColor: '#d33',
                confirmButtonText: 'Yes, create it!'
                }).then((result) => {
                if (result.isConfirmed) {
                    this.createAccount()
                    Swal.fire(
                        'Created!',
                        'You have created an account.',
                        'success'
                        )   
                    }
                })
            },

        createAccount(){
            axios.post('/api/clients/current/accounts')
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

        logout(){
            axios.post('/api/logout').then(() => window.location.assign('../home/index.html'))
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