const app = Vue.createApp({
    data() {
        return {
            url: 'http://127.0.0.1:8080/api/loans',
            name: '',
            maxAmount: '',
            payments: []
        }
    },

    // created() {
    //     this.loadData(this.url)
    // },

    methods: {
        // loadData(url){
        //     axios.get(url).then(response => {
        //         this.loans = response.data
        //         this.loans.forEach(element => {
        //             this.maxId = element.id    
        //         })

        //     })

        //     .catch(error => {
        //         let errorData = error.response.data
        //         Swal.fire({
        //             icon: 'error',
        //             title: 'Oops...',
        //             text: `${errorData}`
        //         })
        //     })
        // },

        createLoan(){
            axios.post("/api/loan", 
            {
                "name": this.name,
                "maxAmount": this.maxAmount,
                "payments": this.payments
            })
            .then(() => clear())
            .catch(error => {
                let errorData = error.response.data
                Swal.fire({
                    icon: 'error',
                    title: 'Oops...',
                    text: `${errorData}`
                })
            })
        },

        clear(){
            this.name = '';
            this.maxAmount = '';
            this.payments = [];
        }
    },
}).mount('#app')